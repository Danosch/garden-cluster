package com.garden.boundary;

import com.garden.controller.GardenController;
import com.garden.controller.PlantController;
import com.garden.controller.TreeController;
import com.garden.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GardenResourceImpl implements GardenResource {

    @Inject
    GardenController gardenController;

    @Inject
    TreeController treeController;

    @Inject
    PlantController plantController;

    @Override
    public String hello() {
        return "Welcome to our Garden Management System";
    }

    @Override
    public GardenDTO createGarden(CreateGardenRequest request) {
        try {
            Garden garden = gardenController.createGarden(request);
            return toGardenDTO(garden);
        } catch (Exception e) {
            throw new WebApplicationException("Error creating garden: " + e.getMessage(),
                    Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public List<GardenDTO> getAllGardens() {
        try {
            List<Garden> gardens = gardenController.getAllGardens();
            return gardens.stream()
                    .map(this::toGardenDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WebApplicationException("Error fetching gardens: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GardenDTO getGarden(UUID gardenId) {
        try {
            Garden garden = gardenController.findGarden(gardenId);
            if (garden == null) {
                throw new WebApplicationException("Garden not found", Response.Status.NOT_FOUND);
            }
            return toGardenDTO(garden);
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new WebApplicationException("Error fetching garden: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GardenDTO updateGarden(UUID gardenId, UpdateGardenRequest request) {
        try {
            Garden garden = gardenController.updateGarden(gardenId, request);
            if (garden == null) {
                throw new WebApplicationException("Garden not found", Response.Status.NOT_FOUND);
            }
            return toGardenDTO(garden);
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new WebApplicationException("Error updating garden: " + e.getMessage(),
                    Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public void deleteGarden(UUID gardenId) {
        try {
            gardenController.deleteGarden(gardenId);
        } catch (Exception e) {
            throw new WebApplicationException("Error deleting garden: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public TreeDTO createTree(CreateTreeRequest request) {
        try {
            Tree tree = treeController.createTree(request);
            return toTreeDTO(tree);
        } catch (Exception e) {
            throw new WebApplicationException("Error creating tree: " + e.getMessage(),
                    Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public List<TreeDTO> getAllTrees() {
        try {
            List<Tree> trees = treeController.getAllTrees();
            return trees.stream()
                    .map(this::toTreeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WebApplicationException("Error fetching trees: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<TreeDTO> getTreesByGarden(UUID gardenId) {
        try {
            List<Tree> trees = treeController.getTreesByGarden(gardenId);
            return trees.stream()
                    .map(this::toTreeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WebApplicationException("Error fetching trees: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteTree(UUID treeId) {
        try {
            treeController.deleteTree(treeId);
        } catch (Exception e) {
            throw new WebApplicationException("Error deleting tree: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PlantDTO createPlant(CreatePlantRequest request) {
        try {
            Plant plant = plantController.createPlant(request);
            return toPlantDTO(plant);
        } catch (Exception e) {
            throw new WebApplicationException("Error creating plant: " + e.getMessage(),
                    Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public List<PlantDTO> getAllPlants() {
        try {
            List<Plant> plants = plantController.getAllPlants();
            return plants.stream()
                    .map(this::toPlantDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WebApplicationException("Error fetching plants: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PlantDTO> getPlantsByGarden(UUID gardenId) {
        try {
            List<Plant> plants = plantController.getPlantsByGarden(gardenId);
            return plants.stream()
                    .map(this::toPlantDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WebApplicationException("Error fetching plants: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deletePlant(UUID plantId) {
        try {
            plantController.deletePlant(plantId);
        } catch (Exception e) {
            throw new WebApplicationException("Error deleting plant: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private GardenDTO toGardenDTO(Garden garden) {
        List<TreeDTO> treeDTOs = garden.getTrees() != null ?
                garden.getTrees().stream().map(this::toTreeDTO).collect(Collectors.toList()) : null;
        List<PlantDTO> plantDTOs = garden.getPlants() != null ?
                garden.getPlants().stream().map(this::toPlantDTO).collect(Collectors.toList()) : null;

        return new GardenDTO(
                garden.getId(),
                garden.getName(),
                garden.getDescription(),
                garden.getCreated(),
                garden.getLastUpdated(),
                treeDTOs,
                plantDTOs
        );
    }

    private TreeDTO toTreeDTO(Tree tree) {
        return new TreeDTO(
                tree.getId(),
                tree.getName(),
                tree.getSpecies(),
                tree.getAge(),
                tree.getHeight(),
                tree.getCreated(),
                tree.getLastUpdated(),
                tree.getGarden() != null ? tree.getGarden().getId() : null
        );
    }

    private PlantDTO toPlantDTO(Plant plant) {
        return new PlantDTO(
                plant.getId(),
                plant.getName(),
                plant.getSpecies(),
                plant.getColor(),
                plant.getPlantingDate(),
                plant.getCreated(),
                plant.getLastUpdated(),
                plant.getGarden() != null ? plant.getGarden().getId() : null
        );
    }

}
