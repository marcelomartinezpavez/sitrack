package cl.sitack.ws.alphabet.soup.mongo.crud.service;

import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.*;
import cl.sitack.ws.alphabet.soup.mongo.crud.resource.IdResource;
import cl.sitack.ws.alphabet.soup.mongo.crud.resource.ItemPaginado;
import cl.sitack.ws.alphabet.soup.mongo.crud.resource.Paginado;
import cl.sitack.ws.alphabet.soup.mongo.crud.utils.MongoCRUDUtils;
import cl.sitack.ws.alphabet.soup.resource.Sopa;
import cl.sitack.ws.alphabet.soup.utils.Utilitario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class MongoCRUDServiceAbstract <Dto, Domain, Id extends Serializable> implements MongoCRUDService<Dto, Domain, Id>{
    private static final Logger logger = LoggerFactory.getLogger(MongoCRUDServiceAbstract.class);
    @Autowired
    protected MongoTemplate mongoTemplate;
    final Class<Domain> domainType;
    final Class<Dto> dtoType;
    final Class<Id> idType;

    public MongoCRUDServiceAbstract() throws NoDocumentException, NoFieldIdException, IdTypeAndIdDocumentIncompatiblesException {
        Class[] arguments = GenericTypeResolver.resolveTypeArguments(this.getClass(), MongoCRUDServiceAbstract.class);
        this.dtoType = arguments[0];
        this.domainType = arguments[1];
        this.idType = arguments[2];
        Class idClass = MongoCRUDUtils.obtenerCampoIdFromDocument(this.domainType).getType();
        if (idClass != this.idType) {
            throw new IdTypeAndIdDocumentIncompatiblesException(this.domainType, idClass);
        }
    }

    public IdResource<Id> crear(Sopa documentObject) throws ValidacionDeNegocioException {
//        return this.crear(this.convertirDtoEnDomain(documentObject), (SequenceService)null);
        return this.crear(this.convertirDtoEnDomain((Dto) documentObject), (SequenceService)null);
    }

    public IdResource<Id> crear(Domain documentObject, SequenceService sequenceService) {
        try {
            if (sequenceService != null) {
                MongoCRUDUtils.setearIdValueEnDocument(documentObject, sequenceService.getNextValue("secuencia_" + MongoCRUDUtils.obtenerCollectionName(documentObject.getClass())));
            }

            this.mongoTemplate.insert(documentObject);
            return new IdResource((Serializable)MongoCRUDUtils.obtenerIdValueFromDocumentObject(documentObject));
        } catch (NoDocumentException var4) {
            var4.printStackTrace();
        } catch (NoFieldIdException var5) {
            var5.printStackTrace();
        } catch (IdTypeAndIdDocumentIncompatiblesException var6) {
            var6.printStackTrace();
        } catch (NoIdValueException var7) {
            var7.printStackTrace();
        }

        return null;
    }

    public Sopa actualizar(Id id, Sopa dtoObject) throws ValidacionDeNegocioException, RecursoNoEncontradoException {
        try {
            //return this.convertirDomainEnDto(this.actualizarDomain(id, this.convertirDtoEnDomain(dtoObject)));
            return this.actualizarDomain(id, dtoObject);
        } catch (NoIdValueException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    protected Sopa actualizarDomain(Id id, Sopa documentObject) throws NoIdValueException, RecursoNoEncontradoException {
        Object documentObjectActual = null;

        try {
            MongoCRUDUtils.setearIdValueEnDocument(documentObject, id);
            documentObjectActual = this.mongoTemplate.findById(id, this.domainType);
            if (documentObjectActual == null) {
                throw new RecursoNoEncontradoException("No se encontro el elemento con id " + id);
            }
        } catch (NoDocumentException var5) {
            var5.printStackTrace();
        } catch (NoFieldIdException var6) {
            var6.printStackTrace();
        } catch (IdTypeAndIdDocumentIncompatiblesException var7) {
            var7.printStackTrace();
        }

        if (documentObjectActual == null) {
            return null;
        } else {
            this.mongoTemplate.save(documentObject);
            return documentObject;
        }
    }

    public Sopa obtenerPorId(Id id) throws RecursoNoEncontradoException {
        Sopa documentObject = (Sopa) this.mongoTemplate.findById(id, this.domainType);
        if (documentObject == null) {
            throw new RecursoNoEncontradoException("No se encontro el elemento con id " + id);
        } else {
            //return this.convertirDomainEnDto(documentObject);
            return documentObject;
        }
    }

    public List<Dto> obtenerLista() throws RecursoNoEncontradoException {
        List aux = this.getMongoRepository().findAll();
        if (aux != null && !aux.isEmpty()) {
            return this.convertirListaDomainEnDto(aux);
        } else {
            throw new RecursoNoEncontradoException("No se encontraron elementos");
        }
    }

    public List<Dto> obtenerListaPaginada(Pageable pageable) throws RecursoNoEncontradoException {
        List aux = (List)this.getMongoRepository().findAll(pageable);
        if (aux != null && !aux.isEmpty()) {
            return this.convertirListaDomainEnDto(aux);
        } else {
            throw new RecursoNoEncontradoException("No se encontraron elementos");
        }
    }

    public List<Dto> obtenerListaPorFiltrosPaginada(Dto dtoObjectExample, Pageable pageable) throws RecursoNoEncontradoException {
        Domain documentObjectExample = this.convertirDtoEnDomain(dtoObjectExample);
        Example<Domain> exampleDocument = Example.of(documentObjectExample);
        List listDomain = (List)this.getMongoRepository().findAll(exampleDocument, pageable);
        if (listDomain != null && !listDomain.isEmpty()) {
            return this.convertirListaDomainEnDto(listDomain);
        } else {
            throw new RecursoNoEncontradoException("No se encontraron elementos");
        }
    }

    public List<Dto> obtenerListaPorFiltros(Dto dtoObjectExample) throws RecursoNoEncontradoException {
        Domain documentObjectExample = this.convertirDtoEnDomain(dtoObjectExample);
        return this.convertirListaDomainEnDto(this.obtenerListaPorFiltros(documentObjectExample, (Consumer)null, (String)null, (String)null));
    }

    public List<Dto> obtenerListaPorFiltros(Dto dtoObjectExample, String anyFieldLike, String anyFieldEquals) throws RecursoNoEncontradoException {
        Domain documentObjectExample = this.convertirDtoEnDomain(dtoObjectExample);
        return this.convertirListaDomainEnDto(this.obtenerListaPorFiltros(documentObjectExample, (Consumer)null, anyFieldLike, anyFieldEquals));
    }

    protected List<Domain> obtenerListaPorFiltros(Domain documentObjectExample, Consumer<Query> procesaParametros, String anyFieldLike, String anyFieldEquals) throws RecursoNoEncontradoException {
        Query query = this.armaQuery(documentObjectExample, procesaParametros, anyFieldLike, anyFieldEquals);
        List auxList = this.mongoTemplate.find(query, this.domainType);
        return auxList;
    }

    public ItemPaginado<Dto> obtenerItemPaginado(Integer limit, Long page, String url) {
        return this.obtenerItemPaginado(limit, page, url, (Domain) null, (Consumer)null, (String)null, (String)null);
    }

    public ItemPaginado<Dto> obtenerItemPaginado(Integer limit, Long page, String url, Domain documentObjectExample) {
        return this.obtenerItemPaginado(limit, page, url, documentObjectExample, (Consumer)null, (String)null, (String)null);
    }

    public ItemPaginado<Dto> obtenerItemPaginado(Integer limit, Long page, String url, Domain documentObjectExample, Consumer<Query> procesaParametros, String anyFieldLike, String anyFieldEquals) {
        Query query = this.armaQuery(documentObjectExample, procesaParametros, anyFieldLike, anyFieldEquals);
        List<Domain> domains = this.obtenerListaPaginada(limit, page, query);
        List<Dto> dtos = this.convertirListaDomainEnDto(domains);
        Long total = this.count(query);
        Long prev = page > 1L && total > 0L ? page - 1L : null;
        Long last = total == 0L ? null : total / (long)limit + (long)(total % (long)limit > 0L ? 1 : 0);
        Long next = total == 0L ? null : (page < last ? page + 1L : null);
        return new ItemPaginado(dtos, new Paginado(total < 1L ? null : url.replace("{page}", "1").replace("{limit}", limit.toString()), total < 1L ? null : url.replace("{page}", last.toString()).replace("{limit}", limit.toString()), next == null ? null : url.replace("{page}", next.toString()).replace("{limit}", limit.toString()), prev == null ? null : url.replace("{page}", prev.toString()).replace("{limit}", limit.toString()), limit, page, total));
    }

    protected List<Domain> obtenerListaPaginada(Integer limit, Long page, Domain documentObjectExample, Consumer<Query> procesaParametros, String anyFieldLike, String anyFieldEquals) {
        Query query = this.armaQuery(documentObjectExample, procesaParametros, anyFieldLike, anyFieldEquals);
        return this.obtenerListaPaginada(limit, page, query);
    }

    protected List<Domain> obtenerListaPaginada(Integer limit, Long page, Query query) {
        return this.mongoTemplate.find(query.skip((long)limit * (page - 1L)).limit(limit), this.domainType);
    }

    protected Long count(Query query) {
        return this.mongoTemplate.count(query, this.domainType);
    }

    protected Query armaQuery(Domain documentObjectExample, Consumer<Query> procesaParametros, String anyFieldLike, String anyFieldEquals) {
        Query query = new Query();
        List<Criteria> criterias = new ArrayList();
        if (documentObjectExample == null) {
            try {
                documentObjectExample = this.domainType.newInstance();
            } catch (InstantiationException var16) {
                var16.printStackTrace();
            } catch (IllegalAccessException var17) {
                var17.printStackTrace();
            }
        }

        Example<Domain> exampleDocument = Example.of(documentObjectExample);
        Criteria criteria = Criteria.byExample(exampleDocument);
        criterias.add(criteria);
        if (anyFieldLike != null || anyFieldEquals != null) {
            Field[] fields = documentObjectExample.getClass().getDeclaredFields();
            List<Criteria> criteriasLike = new ArrayList();
            List<Criteria> criteriasEquals = new ArrayList();
            Field[] var12 = fields;
            int var13 = fields.length;

            for(int var14 = 0; var14 < var13; ++var14) {
                Field field = var12[var14];
                if (field.getType() == Integer.class || field.getType() == String.class || field.getType() == Double.class) {
                    if (anyFieldLike != null) {
                        criteriasLike.add(Criteria.where(field.getName()).regex(".*" + anyFieldLike + ".*", "i"));
                    }

                    if (anyFieldEquals != null) {
                        criteriasEquals.add(Criteria.where(field.getName()).is(anyFieldEquals));
                    }
                }
            }

            Criteria[] aux;
            Criteria or;
            if (!criteriasLike.isEmpty()) {
                aux = new Criteria[criteriasLike.size()];
                aux = (Criteria[])criteriasLike.toArray(aux);
                or = (new Criteria()).orOperator(aux);
                criterias.add(or);
            }

            if (!criteriasEquals.isEmpty()) {
                aux = new Criteria[criteriasEquals.size()];
                aux = (Criteria[])criteriasEquals.toArray(aux);
                or = (new Criteria()).orOperator(aux);
                criterias.add(or);
            }
        }

        Criteria[] aux = new Criteria[criterias.size()];
        aux = (Criteria[])criterias.toArray(aux);
        query.addCriteria((new Criteria()).andOperator(aux));
        if (procesaParametros != null) {
            procesaParametros.accept(query);
        }

        return query;
    }

    public Dto eliminar(Id id) throws ValidacionDeNegocioException, RecursoNoEncontradoException {
        Domain documentObject = this.mongoTemplate.findById(id, this.domainType);
        if (documentObject == null) {
            throw new RecursoNoEncontradoException("No se encontro el elemento con id " + id);
        } else {
            this.getMongoRepository().delete(documentObject);
            return this.convertirDomainEnDto(documentObject);
        }
    }

    protected List<Dto> convertirListaDomainEnDto(List<Domain> domains) {
        if (domains == null) {
            return null;
        } else {
            List<Dto> dtos = new ArrayList();
            domains.forEach((domain) -> {
                dtos.add(this.convertirDomainEnDto(domain));
            });
            return dtos;
        }
    }

    protected List<Domain> convertirListaDtoEnDomain(List<Dto> dtos) {
        if (dtos == null) {
            return null;
        } else {
            List<Domain> domains = new ArrayList();
            dtos.forEach((dto) -> {
                domains.add(this.convertirDtoEnDomain(dto));
            });
            return domains;
        }
    }

    protected Dto convertirDomainEnDto(Domain domain) {
        return Utilitario.convertirObjeto(domain, this.dtoType);
    }

    protected Domain convertirDtoEnDomain(Dto dto) {
        return Utilitario.convertirObjeto(dto, this.domainType);
    }

    protected MongoRepository<Domain, Id> getMongoRepository() {
        return null;
    }
}
