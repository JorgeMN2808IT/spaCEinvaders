package com.spaceinvaders.domain.state;

import com.spaceinvaders.domain.entity.ProjectileOwner;

public class ProjectileState {

    private final int id;
    private final int x;
    private final int y;
    private final ProjectileOwner owner;

    public ProjectileState(int id, int x, int y, ProjectileOwner owner) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ProjectileOwner getOwner() {
        return owner;
    }
}