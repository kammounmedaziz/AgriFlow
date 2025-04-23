package Agriflow.Service;

import Agriflow.Models.Champ;
import Agriflow.Models.Culture;
import Agriflow.Models.enums.CropType;
import Agriflow.Models.enums.GrowthStage;
import Agriflow.Models.enums.IrrigationType;
import Agriflow.Util.MyConnections;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceCulture implements IService<Culture> {
    private Connection cnx;

    public ServiceCulture() {
        cnx = MyConnections.getInstance().getMyConnections();
    }

    @Override
    public void create(Culture culture) throws SQLException {
        String req = "INSERT INTO `culture` (`typePlante`, `datePlantation`, `stadeCroissance`, `Typeirrigation`, `champ_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, culture.getTypePlante().name());
        ps.setDate(2, Date.valueOf(culture.getDatePlantation()));
        ps.setString(3, culture.getStadeCroissance().name());
        ps.setString(4, culture.getTypeirrigation().name());

        if (culture.getChamp() != null) {
            ps.setInt(5, culture.getChamp().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }

        ps.executeUpdate();

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                culture.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public Culture read(int id) throws SQLException {
        String req = "SELECT * FROM `culture` WHERE `id` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Culture culture = new Culture();
            culture.setId(rs.getInt("id"));
            culture.setTypePlante(rs.getString("typePlante"));
            culture.setDatePlantation(rs.getDate("datePlantation").toLocalDate());
            culture.setStadeCroissance(rs.getString("stadeCroissance"));
            culture.setTypeirrigation(rs.getString("Typeirrigation"));

            int champId = rs.getInt("champ_id");
            if (!rs.wasNull()) {
                Champ champ = new ServiceChamp().read(champId);
                culture.setChamp(champ);
            }
            return culture;
        }
        return null;
    }

    @Override
    public void update(Culture culture) throws SQLException {
        String req = "UPDATE `culture` SET `typePlante`=?, `datePlantation`=?, `stadeCroissance`=?, `Typeirrigation`=?, `champ_id`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, culture.getTypePlante().name());
        ps.setDate(2, Date.valueOf(culture.getDatePlantation()));
        ps.setString(3, culture.getStadeCroissance().name());
        ps.setString(4, culture.getTypeirrigation().name());

        if (culture.getChamp() != null) {
            ps.setInt(5, culture.getChamp().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }

        ps.setInt(6, culture.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM `culture` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Culture> readAll() throws SQLException {
        List<Culture> cultures = new ArrayList<>();
        String req = "SELECT * FROM `culture`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Culture culture = new Culture();
            culture.setId(rs.getInt("id"));
            culture.setTypePlante(rs.getString("typePlante"));
            culture.setDatePlantation(rs.getDate("datePlantation").toLocalDate());
            culture.setStadeCroissance(rs.getString("stadeCroissance"));
            culture.setTypeirrigation(rs.getString("Typeirrigation"));

            int champId = rs.getInt("champ_id");
            if (!rs.wasNull()) {
                Champ champ = new ServiceChamp().read(champId);
                culture.setChamp(champ);
            }
            cultures.add(culture);
        }
        return cultures;
    }
}