package cl.sitack.ws.alphabet.soup.mongo.crud.service;

import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.RecursoNoEncontradoException;
import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.ValidacionDeNegocioException;
import cl.sitack.ws.alphabet.soup.mongo.crud.resource.IdResource;
import cl.sitack.ws.alphabet.soup.mongo.crud.resource.ItemPaginado;
import cl.sitack.ws.alphabet.soup.resource.Sopa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface MongoCRUDService <Dto, Domain, Id extends Serializable> {

    IdResource<Id> crear(Sopa documentObject) throws ValidacionDeNegocioException;

    Dto actualizar(Id id, Dto dtoObject) throws ValidacionDeNegocioException, RecursoNoEncontradoException;

    Sopa obtenerPorId(Id id) throws RecursoNoEncontradoException;

    List<Dto> obtenerLista() throws RecursoNoEncontradoException;

    List<Dto> obtenerListaPaginada(Pageable pageable) throws RecursoNoEncontradoException;

    List<Dto> obtenerListaPorFiltrosPaginada(Dto dtoObjectExample, Pageable pageable) throws RecursoNoEncontradoException;

    List<Dto> obtenerListaPorFiltros(Dto dtoObjectExample) throws RecursoNoEncontradoException;

    List<Dto> obtenerListaPorFiltros(Dto dtoObjectExample, String anyFieldLike, String anyFieldEquals) throws RecursoNoEncontradoException;

    ItemPaginado<Dto> obtenerItemPaginado(Integer limit, Long page, String url);

    ItemPaginado<Dto> obtenerItemPaginado(Integer limit, Long page, String url, Domain documentObjectExample);

    ItemPaginado<Dto> obtenerItemPaginado(Integer limit, Long page, String url, Domain documentObjectExample, Consumer<Query> procesaParametros, String anyFieldLike, String anyFieldEquals);

    Dto eliminar(Id id) throws ValidacionDeNegocioException, RecursoNoEncontradoException;
}
