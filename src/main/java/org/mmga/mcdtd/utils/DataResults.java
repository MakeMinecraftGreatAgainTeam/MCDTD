package org.mmga.mcdtd.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * @author wzp
 * @version 1.0.0
 */
public class DataResults {
    private final ResultSet resultSet;
    private final Statement statement;
    private final Connection connection;

    public DataResults(ResultSet resultSet, Statement statement, Connection connection) {
        this.resultSet = resultSet;
        this.statement = statement;
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "DataResults{" +
                "resultSet=" + resultSet +
                ", statement=" + statement +
                ", connection=" + connection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof DataResults)) {return false;}
        DataResults that = (DataResults) o;
        return Objects.equals(resultSet, that.resultSet) && Objects.equals(statement, that.statement) && Objects.equals(connection, that.connection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultSet, statement, connection);
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }
    public void close(){
        try {
            this.resultSet.close();
            this.statement.close();
            this.connection.close();
        } catch (SQLException e) {
            DataUtils.showError(e,"Close database connection failed");
        }
    }
}
