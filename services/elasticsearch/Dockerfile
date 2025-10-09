FROM elasticsearch:8.17.6

RUN ./bin/elasticsearch-plugin install analysis-icu \
    && ./bin/elasticsearch-plugin install analysis-kuromoji \
    && ./bin/elasticsearch-plugin install analysis-smartcn \
    && ./bin/elasticsearch-plugin install analysis-stempel
