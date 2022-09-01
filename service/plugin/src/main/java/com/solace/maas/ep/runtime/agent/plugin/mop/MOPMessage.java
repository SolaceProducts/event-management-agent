package com.solace.maas.ep.runtime.agent.plugin.mop;

// Meta only
//        mop.decoder          string        For content-type of string/json, decoder is the canonical name of the DTO class
//        mop.contenttype      enumeration   Supported types are string/json
//        mop.msg.sig          string        signed content

// Meta and Header
//        mop.ver              string <"M.m"> Used to indicate the mopVer "Major.minor" of the transported msg
//        mop.mopProtocol      enumeration    Used to indicate the MOP sub-mopProtocol type
//        mop.msgtype          enumeration    Used to indicate the message's MOP sub-mopProtocol message type


// Header
//        orig.svc.id          string        The id of the originator of this message
//        orig.svc.type        enumeration   the maas-service type {maas-core, maas-cloud-agent, maas-gateway, ...}
//        orig.svc.mailbox     string        the message provider mailbox address of the originator
//        orig.dcId            string        the data center id of the originator
//        orig.provider        string        provider (cloud) of the originator
//        orig.zone            string        Optional
//        msg.uh               enumeration   The "unhandled" flag one of {ignore,error}

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solacesystems.jcsmp.BytesXMLMessage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;

/**
 * Contains MOP Message header and meta information (meta is carried by the messaging provider)
 */
@Data
public abstract class MOPMessage implements Serializable {

    public enum DestinationType {
        TOPIC,
        QUEUE,
    }

    /**
     * The original JCSMP raw message.
     * NOTE: Should be used only as metadata or logging purposes as any application data must be part of the
     * decoded MOP message.
     */
    @JsonIgnore
    private transient BytesXMLMessage rawMessage;

    // header and meta
    private String mopVer;
    private MOPProtocol mopProtocol;
    private MOPMessageType mopMsgType;

    // header
    private MOPUHFlag msgUh;
    private String origSvcId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MOPSvcType origSvcType;
    private String origSvcMailbox;
    private String origDcId;
    private String origProvider;
    private Boolean repeat = FALSE;
    private String computeInstanceId;
    private String correlationId;
    private Boolean isReplyMessage = false;

    // MOPMessage Destination Type
    private String destinationName;
    private DestinationType destinationType;

    // This field indicates the priority of the message.
    // Default is 4. Valid number is from 0(lowest) to 9(highest)
    // This field can only take effect when the destination queue or topic endpoint has toggled `Respect Message
    // Priority`
    // The default value for `Respect Message Priority` is false
    private int msgPriority = 4;

    private String traceId;

    // Strategies
    private AckStrategy ackStrategy;

    public MOPSvcType getOrigType() {
        return origSvcType;
    }

    public void setOrigType(MOPSvcType origType) {
        origSvcType = origType;
    }

    public MOPMessage withCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public MOPMessage isReplyMessage(Boolean isReplyMessage) {
        this.isReplyMessage = isReplyMessage;
        return this;
    }

    public MOPMessage withPriority(int priority) {
        msgPriority = priority;
        return this;
    }

    public MOPMessage withVersion(String version) {
        mopVer = version;
        return this;
    }

    public MOPMessage withProtocol(MOPProtocol protocol) {
        mopProtocol = protocol;
        return this;
    }

    public MOPMessage withMessageType(MOPMessageType messageType) {
        mopMsgType = messageType;
        return this;
    }

    public MOPMessage withUhFlag(MOPUHFlag uhFlag) {
        msgUh = uhFlag;
        return this;
    }

    public MOPMessage withOrigId(String origId) {
        origSvcId = origId;
        return this;
    }

    public MOPMessage withOrigSvcType(MOPSvcType svcType) {
        origSvcType = svcType;
        return this;
    }

    public MOPMessage withOrigSvcId(String origSvcId) {
        this.origSvcId = origSvcId;
        return this;
    }

    public MOPMessage withOrigMailbox(String origMailbox) {
        origSvcMailbox = origMailbox;
        return this;
    }

    public MOPMessage withOrigProvider(String origProvider) {
        this.origProvider = origProvider;
        return this;
    }

    public MOPMessage withOrigDcId(String dcId) {
        origDcId = dcId;
        return this;
    }

    public void ack() {
        if (ackStrategy != null) {
            ackStrategy.ack();
        }
    }

    public Boolean getRepeat() {
        return repeat == null ? FALSE : repeat;
    }

    public String toLog() {
        return null;
    }

}
