package com.spaceinvaders.patterns.factory;

import com.spaceinvaders.domain.entity.AlienType;
import com.spaceinvaders.domain.entity.AlienUnit;
import com.spaceinvaders.domain.entity.Projectile;
import com.spaceinvaders.domain.entity.ProjectileOwner;
import com.spaceinvaders.domain.entity.ShieldBlock;
import com.spaceinvaders.domain.entity.UfoBonus;
import com.spaceinvaders.domain.state.Direction;

public interface EntityFactory {

    AlienUnit createAlien(int id, AlienType type, int x, int y);

    Projectile createProjectile(int id, ProjectileOwner owner, int x, int y, int speed);

    ShieldBlock createBunker(int id, int x, int y);

    UfoBonus createUfo(int id, int x, int y, int points, Direction direction, int speed);
}