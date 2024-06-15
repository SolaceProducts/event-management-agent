package com.solace.maas.ep.common.model;

import java.util.List;

public interface CommandMessageWithResources {

    List<? extends ResourceConfiguration> getResources();
}
