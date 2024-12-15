package org.enpal.appointmentbooking.config;

import lombok.SneakyThrows;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomStringListType implements UserType<List<String>> {

    @Override
    public int getSqlType() {
        // Return the SQL type for the custom mapping, which is ARRAY for a List<String>
        return Types.ARRAY;
    }

    @Override
    public Class<List<String>> returnedClass() {
        // Return the class of the custom type (List<String>)
        return (Class<List<String>>) (Class<?>) List.class;
    }

    @Override
    public boolean equals(List<String> x, List<String> y) {
        // Define equality logic for Lists
        if (x == null || y == null) {
            return x == y;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(List<String> x) {
        // Define hash code logic for Lists
        return x == null ? 0 : x.hashCode();
    }

    @Override
    @SneakyThrows
    public List<String> nullSafeGet(ResultSet resultSet, int index, SharedSessionContractImplementor session, Object owner) {
        // Retrieve the value from the ResultSet (reading an array from the DB)
        Array array = resultSet.getArray(index);
        if (array == null) {
            return null;
        }
        // Convert the SQL array to a List<String>
        String[] arrayData = (String[]) array.getArray();
        List<String> list = new ArrayList<>();
        for (String s : arrayData) {
            list.add(s);
        }
        return list;
    }

    @Override
    @SneakyThrows
    public void nullSafeSet(PreparedStatement preparedStatement, List<String> value, int index, SharedSessionContractImplementor session) {
        // Set the value in the PreparedStatement (storing a List<String> in the DB)
        if (value == null) {
            preparedStatement.setNull(index, Types.ARRAY);
        } else {
            Connection connection = preparedStatement.getConnection();
            // Convert the List<String> to an array to store as an SQL array
            String[] arrayData = value.toArray(new String[0]);
            Array array = connection.createArrayOf("VARCHAR", arrayData);
            preparedStatement.setArray(index, array);
        }
    }

    @Override
    public List<String> deepCopy(List<String> value) {
        // Create a deep copy of the List<String>
        if (value == null) {
            return null;
        }
        return new ArrayList<>(value);
    }

    @Override
    public boolean isMutable() {
        // Return whether the custom type is mutable (List is mutable)
        return true;
    }

    @Override
    public Serializable disassemble(List<String> value) {
        // Convert the List<String> to a Serializable form for caching or session management
        return (Serializable) value;
    }

    @Override
    public List<String> assemble(Serializable cached, Object owner) {
        // Convert the Serializable form back into a List<String>
        return (List<String>) cached;
    }

    @Override
    public List<String> replace(List<String> original, List<String> target, Object owner) {
        // Replace the original value with the target value
        return target;
    }
}
