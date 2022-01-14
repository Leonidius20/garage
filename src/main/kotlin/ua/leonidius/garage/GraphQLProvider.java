package ua.leonidius.garage;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.leonidius.garage.business.SearchFacade;
import ua.leonidius.garage.dto.CarDetailDto;

import java.util.List;
import java.util.Optional;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider implements GraphQLQueryResolver {
    @Autowired
    private SearchFacade vehicleService;
    public List<CarDetailDto> getDetails(final int count) {
        return this.vehicleService.getAllDetails(count).stream().toList();
    }
    public Optional<CarDetailDto> getDetailById(final String id) {
        return Optional.of(this.vehicleService.getDetailById(id));
    }
}