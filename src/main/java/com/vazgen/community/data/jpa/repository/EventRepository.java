package com.vazgen.community.data.jpa.repository;

import com.vazgen.community.data.jpa.entity.EventEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

  List<EventEntity> findAllByDeletedIsFalse();

  Optional<EventEntity> findByIdAndDeletedIsFalse(Long id);

}
