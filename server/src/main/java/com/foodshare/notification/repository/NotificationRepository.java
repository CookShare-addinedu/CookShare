package com.foodshare.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodshare.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
