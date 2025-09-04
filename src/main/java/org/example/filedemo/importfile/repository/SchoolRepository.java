package org.example.filedemo.importfile.repository;

import org.example.filedemo.importfile.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}