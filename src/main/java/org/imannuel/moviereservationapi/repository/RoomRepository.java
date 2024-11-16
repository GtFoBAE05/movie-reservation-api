package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO m_room(name) VALUES (:#{#room.name})", nativeQuery = true)
    void insertRoom(@Param("room") Room room);

    @Query(value = "SELECT  id, name from m_room LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Room> getAllRoom(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT mr.id, mr.name, ms.id as seat_id, ms.seat_code, ms.room_id as room_id from m_room mr left join m_seat ms on mr.id = ms.room_id where mr.id = :id", nativeQuery = true)
    Optional<Room> findRoomById(@Param("id") Long id);

    @Query(value = "SELECT mr.id, mr.name, ms.id as seat_id, ms.seat_code, ms.room_id as room_id from m_room mr left join m_seat ms on mr.id = ms.room_id where mr.name = :name", nativeQuery = true)
    Optional<Room> findRoomByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query(value = "UPDATE m_room SET name = :#{#room.name} WHERE id = :#{#room.id}", nativeQuery = true)
    void updateRoom(@Param("room") Room room);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM m_room WHERE id = :id", nativeQuery = true)
    void deleteRoom(@Param("id") long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM m_seat WHERE room_id = :roomId", nativeQuery = true)
    void deleteSeatsByRoomId(@Param("roomId") Long roomId);

    @Query(value = "SELECT EXISTS (SELECT name FROM m_room WHERE name ILIKE :name)", nativeQuery = true)
    boolean existsRoomByName(@Param("name") String name);

    @Query(value = "SELECT COUNT(*) FROM m_room", nativeQuery = true)
    long countTotalRooms();
}
