package com.solace.maas.ep.event.management.agent.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class CommandLineImport {

    private final ImportService importService;
    private final CommandLineCommon commandLineCommon;

    public CommandLineImport(ImportService importService,
                             CommandLineCommon commandLineCommon) {
        this.importService = importService;
        this.commandLineCommon = commandLineCommon;
    }

    public void runUpload(String filePathAndName) {
        try {
            String scanId = getScanIdFromScanZip(filePathAndName);

            MultipartFile multipartFile = MultipartFileWrapper.builder().absolutePath(filePathAndName).build();
            ImportRequestBO importRequestBO = ImportRequestBO.builder()
                    .dataFile(multipartFile)
                    .traceId(UUID.randomUUID().toString())
                    .build();

            importService.importData(importRequestBO);
            log.info("Import request [{}]: Import started.", importRequestBO.getTraceId());
            commandLineCommon.waitForOperationToComplete(scanId);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getScanIdFromScanZip(String fileZip) throws IOException {
        String metaInfFile = readMetaInfo(fileZip);
        if (metaInfFile != null) {
            MetaInfFileBO metaInfFileBO = new ObjectMapper().readValue(metaInfFile, MetaInfFileBO.class);
            return metaInfFileBO.getScanId();
        } else {
            throw new RuntimeException("This does not appear to be a scan zip file. Unable to find META_INF.json file in zip file " + fileZip + ".");
        }

    }

    @SuppressWarnings("PMD.AssignmentInOperand")
    public static String readMetaInfo(String fileZip) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(fileZip)))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if ("META_INF.json".equals(zipEntry.getName())) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int len;

                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }
                    baos.close();
                    return baos.toString(UTF_8);
                }
                zipEntry = zis.getNextEntry();
            }
            return null;
        }
    }

}
