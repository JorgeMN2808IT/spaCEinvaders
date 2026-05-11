package com.spaceinvaders.collections;

import java.util.function.Predicate;

/**
 * Lista enlazada simple genérica creada para manejar estructuras dinámicas
 * dentro del juego.
 *
 * Esta lista será utilizada para almacenar entidades como jugadores,
 * extraterrestres, disparos, bunkers y ovnis.
 *
 * @param <T> Tipo de dato almacenado en la lista.
 */
public class GameList<T> {

    private Node<T> head;
    private int size;

    public GameList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param data Elemento a insertar.
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
            size++;
            return;
        }

        Node<T> current = head;

        while (current.getNext() != null) {
            current = current.getNext();
        }

        current.setNext(newNode);
        size++;
    }

    /**
     * Agrega un elemento al inicio de la lista.
     *
     * @param data Elemento a insertar.
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        newNode.setNext(head);
        head = newNode;

        size++;
    }

    /**
     * Obtiene el elemento en una posición específica.
     *
     * @param index Índice del elemento.
     * @return Elemento encontrado.
     */
    public T get(int index) {
        validateIndex(index);

        Node<T> current = head;
        int counter = 0;

        while (counter < index) {
            current = current.getNext();
            counter++;
        }

        return current.getData();
    }

    /**
     * Busca el primer elemento que cumpla una condición.
     *
     * @param predicate Condición de búsqueda.
     * @return Elemento encontrado o null si no existe.
     */
    public T find(Predicate<T> predicate) {
        Node<T> current = head;

        while (current != null) {
            if (predicate.test(current.getData())) {
                return current.getData();
            }

            current = current.getNext();
        }

        return null;
    }

    /**
     * Elimina el primer elemento que cumpla una condición.
     *
     * @param predicate Condición de eliminación.
     * @return true si se eliminó un elemento, false si no se encontró.
     */
    public boolean remove(Predicate<T> predicate) {
        if (head == null) {
            return false;
        }

        if (predicate.test(head.getData())) {
            head = head.getNext();
            size--;
            return true;
        }

        Node<T> previous = head;
        Node<T> current = head.getNext();

        while (current != null) {
            if (predicate.test(current.getData())) {
                previous.setNext(current.getNext());
                size--;
                return true;
            }

            previous = current;
            current = current.getNext();
        }

        return false;
    }

    /**
     * Recorre todos los elementos de la lista.
     *
     * @param action Acción que se ejecutará sobre cada elemento.
     */
    public void forEach(GameListAction<T> action) {
        Node<T> current = head;

        while (current != null) {
            action.execute(current.getData());
            current = current.getNext();
        }
    }

    /**
     * Verifica si existe al menos un elemento que cumpla una condición.
     *
     * @param predicate Condición a evaluar.
     * @return true si existe, false si no.
     */
    public boolean contains(Predicate<T> predicate) {
        return find(predicate) != null;
    }

    /**
     * Elimina todos los elementos de la lista.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Retorna la cantidad de elementos almacenados.
     *
     * @return Tamaño de la lista.
     */
    public int size() {
        return size;
    }

    /**
     * Verifica si la lista está vacía.
     *
     * @return true si está vacía, false si contiene elementos.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna el primer nodo de la lista.
     *
     * Este método se usará para recorridos personalizados.
     *
     * @return Nodo inicial.
     */
    public Node<T> getHead() {
        return head;
    }

    /**
     * Valida que un índice sea correcto.
     *
     * @param index Índice a validar.
     */
    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + index);
        }
    }
}