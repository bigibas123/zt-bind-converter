package com.github.bigibas123.zt.bind.converter.model.networklist;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Network {

   private String id;
   private String type;
   @SerializedName( "clock" )
   private long lastUpdate;
   private NetworkConfig config;
   private String description;
   private String rulesSource;
   private NetworkPermissions permissions;
   private UUID ownerId;
   private int onlineMemberCount;
   private int authorizedMemberCount;
   private int totalMemberCount;
   private Map<String, Integer> capabilitiesByName;
   private Map<String, Tag> tagsByName;
   private String ui;

}
