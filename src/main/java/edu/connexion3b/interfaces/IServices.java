package edu.connexion3b.interfaces;

import java.util.List;

public interface IServices<T> {

    void ajouterEntity(T t); //Create
    void supprimerEntity(int id); //Delete
    void updateEntity(int idt, T t); //Update
    List<T> getAllData(); //Read --> Select * From
}

