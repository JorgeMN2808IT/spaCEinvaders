package com.spaceinvaders.collections;

/**
 * @param <T> Tipo de dato recorrido.
 */
public class GameIterator<T> {

    private Node<T> current;

    public GameIterator(GameList<T> list) {
        this.current = list.getHead();
    }

    /**
     * @return true si hay un elemento disponible.
     */
    public boolean hasNext() {
        return current != null;
    }

    /**
     * @return Dato actual.
     */
    public T next() {
        if (current == null) {
            return null;
        }

        T data = current.getData();
        current = current.getNext();

        return data;
    }
}