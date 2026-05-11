package com.spaceinvaders.patterns.factory;

import com.spaceinvaders.domain.entity.AlienType;
import com.spaceinvaders.domain.entity.AlienUnit;
import com.spaceinvaders.domain.entity.Projectile;
import com.spaceinvaders.domain.entity.ProjectileOwner;
import com.spaceinvaders.domain.entity.ShieldBlock;
import com.spaceinvaders.domain.entity.UfoBonus;
import com.spaceinvaders.domain.state.Direction;

public class DefaultEntityFactory implements EntityFactory {

    @Override
    public AlienUnit createAlien(int id, AlienType type, int x, int y) {
        return new AlienUnit(id, type, x, y);
    }

    @Override
    public Projectile createProjectile(int id, ProjectileOwner owner, int x, int y, int speed) {
        return new Projectile(id, owner, x, y, speed);
    }

    @Override
    public ShieldBlock createBunker(int id, int x, int y) {
        return new ShieldBlock(id, x, y);
    }

    @Override
    public UfoBonus createUfo(int id, int x, int y, int points, Direction direction, int speed) {
        return new UfoBonus(id, x, y, points, direction, speed);
    }
}