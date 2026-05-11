package com.spaceinvaders.collections;

/**
 * @param <T> Tipo de dato recibido por la acción.
 */
@FunctionalInterface
public interface GameListAction<T> {

    void execute(T item);
}