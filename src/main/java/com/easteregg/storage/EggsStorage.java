package com.easteregg.storage;

import com.easteregg.EasterEgg;
import com.easteregg.managers.ManagerHandler;
import com.easteregg.utils.serializer.LocationSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class EggsStorage {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void saveEggs(final ManagerHandler managerHandler){
        List<EasterEggsData> dataList = new ArrayList<>();
        File dataFolder = managerHandler.getEasterMain().getDataFolder();

        managerHandler.getEasterEggsManager().getEasterEggs().forEach(egg->dataList.add(egg.toData()));

        try {
            objectMapper.writeValue(new File(dataFolder,"eggs.json"),dataList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadEggs(final ManagerHandler managerHandler){
        File dataFolder = managerHandler.getEasterMain().getDataFolder();
        File eggsFile = new File(dataFolder, "eggs.json");
        if(!eggsFile.exists()) return;

        try {
            List<EasterEggsData> dataList = objectMapper.readValue(eggsFile, new TypeReference<List<EasterEggsData>>() {});

            LinkedHashSet<EasterEgg> eggs = new LinkedHashSet<>();
            dataList.forEach(data->eggs.add(new EasterEgg(LocationSerializer.deserializeLocation(data.getLocation()),data.getUrl(),data.getId())));

            managerHandler.getEasterEggsManager().getEasterEggs().addAll(eggs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
