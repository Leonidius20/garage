// package ua.leonidius.garage

import graphql.schema.GraphQLSchema
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ua.leonidius.garage.business.SearchFacade
import ua.leonidius.garage.business.SearchFacadeImpl

/*@Configuration
class Config {

    @Bean
    fun searchFacade(): GraphQLSchema {
        return GraphQLSchema.newSchema() .query(
            GraphQLObjectType.newObject()
                .name("query")
                .field(field -> field.name("test").type(Scalars.GraphQLString))
        .build())
    }

}*/