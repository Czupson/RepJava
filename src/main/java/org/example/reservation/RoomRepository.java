package org.example.reservation;

import java.util.Optional;

public interface RoomRepository {
    Optional<Room> findById(String roomId);
}
