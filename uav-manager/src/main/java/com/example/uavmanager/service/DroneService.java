package com.example.uavmanager.service;

import com.example.uavmanager.domain.Drone;
import com.example.uavmanager.domain.DroneForm;
import java.util.List;
import java.util.Optional;

public interface DroneService {

    /**
     * List drones.
     *
     * @param q optional search keyword (name/model/serialNo)
     * @return drones
     */
    List<Drone> list(String q);

    /**
     * Get drone by id.
     *
     * @param id drone id
     * @return drone
     */
    Optional<Drone> getById(long id);

    /**
     * Create a new drone with AI attributes generated.
     *
     * @param form create form
     * @return created drone id
     */
    long create(DroneForm form);

    /**
     * Update an existing drone.
     *
     * @param id drone id
     * @param form update form
     * @param regenAi whether to regenerate AI attributes
     */
    void update(long id, DroneForm form, boolean regenAi);

    /**
     * Delete a drone by id.
     *
     * @param id drone id
     */
    void delete(long id);
}

