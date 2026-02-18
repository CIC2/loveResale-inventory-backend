package com.resale.resaleinventory.components.media;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.resale.resaleinventory.components.media.dto.*;
import com.resale.resaleinventory.components.model.dto.ModelByIdDTO;
import com.resale.resaleinventory.components.unit.dto.UnitDetailsDTO;
import com.resale.resaleinventory.utils.ReturnObject;

@Service
public class ModelMediaService {

    @Autowired
    StorageService storageService;

    public ReturnObject<ModelImagesResponseDTO> getModelMedia(
            String projectCode,
            String modelCode
    ) {

        String basePrefix =
                "assets/assets/Models/" + projectCode + "/" + modelCode + "/";

        List<String> keys = storageService.listObjectsByPrefix(basePrefix);

        List<FolderImagesDTO> resultFolders = new ArrayList<>();

        if (keys.isEmpty()) {
            return new ReturnObject<>(
                    "No images found for this model",
                    true,
                    new ModelImagesResponseDTO(projectCode, modelCode, resultFolders)
            );
        }

        Set<String> singleImageFolders = Set.of(
                "Basement",
                "PDF",
                "360",
                "Finishing"
        );

        for (String folder : singleImageFolders) {

            String folderPrefix = basePrefix + folder + "/";

            keys.stream()
                    .filter(k -> k.startsWith(folderPrefix) && !k.endsWith("/"))
                    .findFirst()
                    .ifPresent(key -> resultFolders.add(
                            new FolderImagesDTO(
                                    folder,
                                    List.of(storageService.buildPublicUrl(key)),
                                    null,
                                    null,
                                    null
                            )
                    ));
        }


        Map<Integer, String> floorImages = new HashMap<>();
        Map<Integer, Map<Integer, String>> floorUnitPlans = new HashMap<>();

        for (String key : keys) {

            if (key.matches(".*/Floor/\\d+/\\d+\\..*")) {
                int floorIndex = extractFloorIndex(key);


                floorImages.putIfAbsent(
                        floorIndex,
                        storageService.buildPublicUrl(key)
                );
            } else if (key.matches(".*/Floor/\\d+/UnitPlan/\\d+\\..*")) {
                int floorIndex = extractFloorIndex(key);
                int unitIndex = extractIndexFromFilename(key);

                floorUnitPlans
                        .computeIfAbsent(floorIndex, f -> new HashMap<>())
                        .putIfAbsent(
                                unitIndex,
                                storageService.buildPublicUrl(key)
                        );
            }
        }

        List<IndexedImagesDTO> floorItems = new ArrayList<>();

        List<Integer> sortedFloors = new ArrayList<>(floorImages.keySet());
        Collections.sort(sortedFloors);

        for (int floorIndex : sortedFloors) {

            Map<Integer, String> unitPlansForFloor =
                    floorUnitPlans.getOrDefault(floorIndex, Map.of());

            List<UnitPlanDTO> unitPlanList = new ArrayList<>();

            if (!unitPlansForFloor.isEmpty()) {
                int maxUnitIndex = Collections.max(unitPlansForFloor.keySet());
                for (int i = 0; i <= maxUnitIndex; i++) {
                    unitPlanList.add(
                            new UnitPlanDTO(
                                    "UnitPlan " + i,
                                    unitPlansForFloor.get(i)
                            )
                    );
                }
            }

            floorItems.add(
                    new IndexedImagesDTO(
                            floorIndex,
                            floorImages.get(floorIndex),
                            unitPlanList
                    )
            );
        }

        if (!floorItems.isEmpty()) {
            resultFolders.add(
                    new FolderImagesDTO("Floors", floorItems)
            );
        }

        return new ReturnObject<>(
                "Model images retrieved successfully",
                true,
                new ModelImagesResponseDTO(
                        projectCode,
                        modelCode,
                        resultFolders
                )
        );
    }

    private int extractFloorIndex(String key) {
        Matcher matcher = Pattern.compile("/Floor/(\\d+)/").matcher(key);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalArgumentException("Invalid floor path: " + key);
    }


    public ReturnObject<List<ModelImageDTO>> getProjectModelsImages(String projectCode) {

        String basePrefix = "assets/assets/Models/" + projectCode + "/";

        List<String> modelFolders = storageService.listCommonPrefixes(basePrefix);

        if (modelFolders.isEmpty()) {
            return new ReturnObject<>(
                    "No models found for this project",
                    true,
                    List.of()
            );
        }

        List<ModelImageDTO> result = new ArrayList<>();

        for (String modelFolder : modelFolders) {

            String modelCode = modelFolder
                    .substring(basePrefix.length())
                    .replace("/", "");

            String mimgsPrefix = modelFolder + "MImgs/";

            List<String> imageKeys = storageService.listObjectsByPrefix(mimgsPrefix);

            String imageUrl = imageKeys.stream()
                    .findFirst()
                    .map(key -> storageService.buildPublicUrl(key))
                    .orElse(null);

            result.add(
                    new ModelImageDTO(
                            modelCode,
                            "MImgs",
                            imageUrl
                    )
            );
        }

        return new ReturnObject<>(
                "Project models cover images retrieved successfully",
                true,
                result
        );
    }

    private int extractIndexFromFilename(String key) {
        Matcher matcher = Pattern.compile("/(\\d+)\\.").matcher(key);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalArgumentException("Invalid indexed file: " + key);
    }

    public ModelMediaDTO getMediaForUnit(ModelByIdDTO dto) {
        if (dto.getProjectCode() == null || dto.getModelCode() == null) {
            return null;
        }

        String basePrefix = "assets/assets/Models/" + dto.getProjectCode() + "/" + dto.getModelCode() + "/";
        List<String> keys = storageService.listObjectsByPrefix(basePrefix);

        Map<String, String> singleImages = new HashMap<>();
        Map<String, List<String>> multiImages = new HashMap<>();

        List<String> singleFolders = List.of("Basement", "PDF", "360", "Finishing");
        for (String folder : singleFolders) {
            String prefix = basePrefix + folder + "/";
            keys.stream()
                    .filter(k -> k.startsWith(prefix) && !k.endsWith("/"))
                    .findFirst()
                    .map(storageService::buildPublicUrl)
                    .ifPresent(url -> singleImages.put(folder, url));
        }

        Integer floorNo = extractFloorIndexFromUnitModelCode(dto.getUnitModelCode());
        Integer unitPlanIndex = extractUnitPlanIndexFromUnitModelCode(dto.getUnitModelCode());

        if (floorNo != null) {
            keys.stream()
                    .filter(k -> k.matches(".*/Floor/" + floorNo + "/\\d+\\..*"))
                    .findFirst()
                    .map(storageService::buildPublicUrl)
                    .ifPresent(url -> singleImages.put("Floor", url));
        }

        if (floorNo != null && unitPlanIndex != null) {
            keys.stream()
                    .filter(k -> k.matches(".*/Floor/" + floorNo + "/UnitPlan/" + unitPlanIndex + "\\..*"))
                    .findFirst()
                    .map(storageService::buildPublicUrl)
                    .ifPresent(url -> singleImages.put("UnitPlan", url));
        }

        String mImgsPrefix = basePrefix + "MImgs/";
        List<String> mSubfolders = List.of("Medium", "Main", "Small");
        for (String subfolder : mSubfolders) {
            String prefix = mImgsPrefix + subfolder + "/";
            List<String> urls = keys.stream()
                    .filter(k -> k.startsWith(prefix) && !k.endsWith("/"))
                    .map(storageService::buildPublicUrl)
                    .toList();
            if (!urls.isEmpty()) {
                multiImages.put(subfolder, urls);
            }
        }

        return new ModelMediaDTO(
                singleImages,
                multiImages
        );
    }

    private Integer extractFloorIndexFromUnitModelCode(String unitModelCode) {
        if (unitModelCode == null || !unitModelCode.contains("-")) return null;
        try {
            String[] parts = unitModelCode.split("-");
            if (parts.length >= 2) {
                return Integer.parseInt(parts[1]);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }


    private Integer extractUnitPlanIndexFromUnitModelCode(String unitModelCode) {
        if (unitModelCode == null || !unitModelCode.contains("-")) return null;
        try {
            String[] parts = unitModelCode.split("-");
            if (parts.length >= 3) {
                return Integer.parseInt(parts[2]);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    private Integer extractUnitPlanIndex(String unitModelCode) {
        if (unitModelCode == null || !unitModelCode.contains("-")) return null;
        String[] parts = unitModelCode.split("-");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    public ModelMediaDTO getMediaForUnitDetails(UnitDetailsDTO dto) {
        if (dto.getProjectCode() == null || dto.getModelCode() == null) {
            return null;
        }

        String basePrefix = "assets/assets/Models/" + dto.getProjectCode() + "/" + dto.getModelCode() + "/";
        List<String> keys = storageService.listObjectsByPrefix(basePrefix);

        Map<String, String> singleImages = new HashMap<>();
        Map<String, List<String>> multiImages = new HashMap<>();

        List<String> singleFolders = List.of("Basement", "PDF", "360", "Finishing");
        for (String folder : singleFolders) {
            String prefix = basePrefix + folder + "/";
            keys.stream()
                    .filter(k -> k.startsWith(prefix) && !k.endsWith("/"))
                    .findFirst()
                    .map(storageService::buildPublicUrl)
                    .ifPresent(url -> singleImages.put(folder, url));
        }

        // Extract floor and unit plan indices from unitModelCode
        Integer floorNo = extractFloorIndexFromUnitModelCode(dto.getUnitModelCode());
        Integer unitPlanIndex = extractUnitPlanIndexFromUnitModelCode(dto.getUnitModelCode());

        // Floor image
        if (floorNo != null) {
            String floorPattern = ".*/Floor/" + floorNo + "/\\d+\\..*";

            List<String> matchingFloorKeys = keys.stream()
                    .filter(k -> k.matches(floorPattern))
                    .toList();


            matchingFloorKeys.stream()
                    .findFirst()
                    .map(storageService::buildPublicUrl)
                    .ifPresent(url -> {
                        singleImages.put("Floor", url);
                    });
        }

        // Unit plan image
        if (floorNo != null && unitPlanIndex != null) {
            String unitPlanPattern = ".*/Floor/" + floorNo + "/UnitPlan/" + unitPlanIndex + "\\..*";


            List<String> matchingUnitPlanKeys = keys.stream()
                    .filter(k -> k.matches(unitPlanPattern))
                    .toList();


            matchingUnitPlanKeys.stream()
                    .findFirst()
                    .map(storageService::buildPublicUrl)
                    .ifPresent(url -> {
                        singleImages.put("UnitPlan", url);
                    });
        }

        String mImgsPrefix = basePrefix + "MImgs/";
        List<String> mSubfolders = List.of("Medium", "Main", "Small");
        for (String subfolder : mSubfolders) {
            String prefix = mImgsPrefix + subfolder + "/";
            List<String> urls = keys.stream()
                    .filter(k -> k.startsWith(prefix) && !k.endsWith("/"))
                    .map(storageService::buildPublicUrl)
                    .toList();
            if (!urls.isEmpty()) {
                multiImages.put(subfolder, urls);
            }
        }
        
        return new ModelMediaDTO(
                singleImages,
                multiImages
        );
    }


    private Integer safeParseInt(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }


}


