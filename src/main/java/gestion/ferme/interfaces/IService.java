package gestion.ferme.interfaces;

import java.util.List;

public interface IService<T> {

    void ajouterEntity(T t); //Create
    void supprimerEntity(T t); //Delete
    void updateEntity(int idt, T t); //Update
    List<T> getAllData(); //Read --> Select * From

}


