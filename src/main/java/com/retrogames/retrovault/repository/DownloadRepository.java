package com.retrogames.retrovault.repository;

import com.retrogames.retrovault.entity.Download;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadRepository extends JpaRepository<Download, Long> {
}
