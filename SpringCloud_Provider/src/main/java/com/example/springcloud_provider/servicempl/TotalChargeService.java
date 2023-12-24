package com.example.springcloud_provider.servicempl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class TotalChargeService {
    @Autowired
    private DataSource dataSource;

    public double calculateTotalRevenue() {
        double totalRevenue = 0.0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT SUM(TotalCost) FROM total_charge");
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                totalRevenue = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }
}
