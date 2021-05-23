package swagger;

import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.spi.AuthFilter;

/*
    �������, ������� ����������� � �������, �������� � LinkedList.
    ����� ���, ��� ������� ������, REST Assured �������� ���, ��������� �� ������ � �������� ���� ������ �� ������.
    ����� �� �� ����� �������� � ��������� �������.

    ������ �������� �������, ��� ������� ����������� � ��� �������, � ������� �� ��������,
    ���������� ��� ����������� ��������� ������ ������� ���������, ��������������� ��������� OrderedFilter.
    �� ��������� ��������� ������ �������� ��������� �������, ���� ��� ���� ���������� (1000).
    ������� � ����������� ���� ����� ����������� ������ �������, � ����������� ���� � ����� ���.

    �������, ����� ����� ���������� � �������� ��������� ���� �������� ���������� ���������, ��������, � 999.
    ����� ������ � ������� ����� �������� ���, ������� ��� �������� ������.
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