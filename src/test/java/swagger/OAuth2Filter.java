package swagger;

import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.spi.AuthFilter;

/*
    Фильтры, которые добавляются к запросу, хранятся в LinkedList.
    Перед тем, как сделать запрос, REST Assured изменяет его, проходясь по списку и применяя один фильтр за другим.
    Потом то же самое делается с пришедшим ответом.

    Помимо обычного правила, что фильтры применяются в том порядке, в котором их добавили,
    существует еще возможность выставить своему фильтру приоритет, имплементировав интерфейс OrderedFilter.
    Он позволяет выставить особый числовой приоритет фильтру, выше или ниже дефолтного (1000).
    Фильтры с приоритетом выше будут выполняться раньше обычных, с приоритетом ниже — после них.

    Конечно, здесь можно запутаться и случайно выставить двум фильтрам одинаковый приоритет, например, в 999.
    Тогда первым к запросу будет применен тот, который был добавлен раньше.
 */

public class OAuth2Filter implements AuthFilter {

    String accessToken;

    public OAuth2Filter(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,     // request filter
            FilterableResponseSpecification responseSpec,   // response filter
            FilterContext ctx) {                            // context filter

        requestSpec.replaceHeader("Authorization", accessToken);
        return ctx.next(requestSpec, responseSpec);
    }
}