package com.zhanlin.task_tracker.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false,length = 255)
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Task> ownedTasks;

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    private List<Task> assignedTasks;




}
