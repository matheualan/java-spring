package br.com.remember.client.mapper;

import br.com.remember.client.dto.ClientRequest;
import br.com.remember.client.dto.ClientResponse;
import br.com.remember.client.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponse toResponse(Client client);
    Client toEntity(ClientRequest dto);

}