package ua.leonidius.garage;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.leonidius.garage.business.SearchFacade;
import ua.leonidius.garage.presentation.results.CarDetailReturnResult;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider implements GraphQLQueryResolver {
    @Autowired
    private SearchFacade vehicleService;
    public List<CarDetailReturnResult> getDetails(final int count) {
        return this.vehicleService.getAllDetails(count).stream().toList();
    }
    public Optional<CarDetailReturnResult> getDetailById(final String id) {
        return Optional.of(this.vehicleService.getDetailById(id));
    }
}