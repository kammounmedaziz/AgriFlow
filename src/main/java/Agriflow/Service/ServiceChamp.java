package Agriflow.Service;

import Agriflow.Models.Champ;
import Agriflow.Models.enums.FieldUseType;
import Agriflow.Models.enums.SoilType;
import Agriflow.Service.IService;
import Agriflow.Util.MyConnections;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceChamp implements IService<Champ> {
    private Connection cnx;

    public ServiceChamp() {
        cnx = MyConnections.getInstance().getMyConnections();
    }

    @Override
    public void create(Champ champ) throws SQLException {
        String req = "INSERT INTO `champ` (`nom`, `superficie`, `typeUtilisation`, `typeSol`) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, champ.getNom());
        ps.setDouble(2, champ.getSuperficie());
        ps.setString(3, champ.getTypeUtilisation().name());
        ps.setString(4, champ.getTypeSol().name());

        ps.executeUpdate();

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                champ.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public Champ read(int id) throws SQLException {
        String req = "SELECT * FROM `champ` WHERE `Champ_id` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Champ champ = new Champ();
            champ.setId(rs.getInt("Champ_id"));
            champ.setNom(rs.getString("nom"));
            champ.setSuperficie(rs.getDouble("superficie"));
            champ.setTypeUtilisation(rs.getString("typeUtilisation"));
            champ.setTypeSol(rs.getString("typeSol"));
            return champ;
        }
        return null;
    }

    @Override
    public void update(Champ champ) throws SQLException {
        String req = "UPDATE `champ` SET `nom`=?, `superficie`=?, `typeUtilisation`=?, `typeSol`=? WHERE `Champ_id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, champ.getNom());
        ps.setDouble(2, champ.getSuperficie());
        ps.setString(3, champ.getTypeUtilisation().name());
        ps.setString(4, champ.getTypeSol().name());
        ps.setInt(5, champ.getId());

        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM `champ` WHERE `Champ_id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Champ> readAll() throws SQLException {
        List<Champ> champs = new ArrayList<>();
        String req = "SELECT * FROM `champ`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Champ champ = new Champ();
            champ.setId(rs.getInt("Champ_id"));
            champ.setNom(rs.getString("nom"));
            champ.setSuperficie(rs.getDouble("superficie"));
            champ.setTypeUtilisation(rs.getString("typeUtilisation"));
            champ.setTypeSol(rs.getString("typeSol"));
            champs.add(champ);
        }
        return champs;
    }
}