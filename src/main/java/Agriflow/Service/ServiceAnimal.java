package Agriflow.Service;

import Agriflow.Models.Animal;
import Agriflow.Models.Champ;
import Agriflow.Models.enums.AnimalHealthStatus;
import Agriflow.Models.enums.AnimalIdentificationType;
import Agriflow.Models.enums.AnimalSpecies;
import Agriflow.Service.IService;
import Agriflow.Service.ServiceChamp;
import Agriflow.Util.MyConnections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAnimal implements IService<Animal> {
    private Connection cnx;

    public ServiceAnimal() {
        cnx = MyConnections.getInstance().getMyConnections();
    }

    @Override
    public void create(Animal animal) throws SQLException {
        String req = "INSERT INTO `animal` (`espece`, `identification`, `etatSante`, `champ_id`) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, animal.getEspece().name());
        ps.setString(2, animal.getIdentification().name());
        ps.setString(3, animal.getEtatSante().name());

        if (animal.getChamp() != null) {
            ps.setInt(4, animal.getChamp().getId());
        } else {
            ps.setNull(4, Types.INTEGER);
        }

        ps.executeUpdate();

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                animal.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public Animal read(int id) throws SQLException {
        String req = "SELECT * FROM `animal` WHERE `id` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Animal animal = new Animal();
            animal.setId(rs.getInt("id"));
            animal.setEspece(rs.getString("espece"));
            animal.setIdentification(rs.getString("identification"));
            animal.setEtatSante(rs.getString("etatSante"));

            int champId = rs.getInt("champ_id");
            if (!rs.wasNull()) {
                Champ champ = new ServiceChamp().read(champId);
                animal.setChamp(champ);
            }
            return animal;
        }
        return null;
    }

    @Override
    public void update(Animal animal) throws SQLException {
        String req = "UPDATE `animal` SET `espece`=?, `identification`=?, `etatSante`=?, `champ_id`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, animal.getEspece().name());
        ps.setString(2, animal.getIdentification().name());
        ps.setString(3, animal.getEtatSante().name());

        if (animal.getChamp() != null) {
            ps.setInt(4, animal.getChamp().getId());
        } else {
            ps.setNull(4, Types.INTEGER);
        }

        ps.setInt(5, animal.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM `animal` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Animal> readAll() throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String req = "SELECT * FROM `animal`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Animal animal = new Animal();
            animal.setId(rs.getInt("id"));
            animal.setEspece(rs.getString("espece"));
            animal.setIdentification(rs.getString("identification"));
            animal.setEtatSante(rs.getString("etatSante"));

            int champId = rs.getInt("champ_id");
            if (!rs.wasNull()) {
                Champ champ = new ServiceChamp().read(champId);
                animal.setChamp(champ);
            }
            animals.add(animal);
        }
        return animals;
    }
}