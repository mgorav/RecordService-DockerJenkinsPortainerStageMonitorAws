package com.gm.record.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Plugging in RSQL to produce specification for JPA based searching
 */
public class RecordRsqlVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

    private RsqlBuilder<T> builder;

    public RecordRsqlVisitor() {
        builder = new RsqlBuilder<T>();
    }

    @Override
    public Specification<T> visit(AndNode node, Void param) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(OrNode node, Void param) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(ComparisonNode node, Void params) {
        return builder.createSpecification(node);
    }
}
