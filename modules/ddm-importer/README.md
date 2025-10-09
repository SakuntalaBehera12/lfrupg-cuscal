# Common DDM Importer

Freemarker templates and structures are under `modules/ddm-importer/ddm-importer-service/src/main/resources/`.

### Template/Structure deployment

Making sure templates are deployed to your local instance.

-   run `blade gw deploy` from the `ddm-importer` root directory
    \*\* If the template changes aren't automatically reflected using this module, you can go into gogo shell and run `ddm:deploy`, which should quickly deploy the template changes to your local instance

### ADTs

The Application Display Templates are freemarker templates used in portlets other than Web Content Display in order to render out dynamic data.

### DDMs

Content Structure and Content Template should be place in pairs of JSON and FTL files. See sample for more details.

### TODOs

- Automate all ADTs as much as possible, whenever FE team has bandwidth.
- See placeholders under `adt.Cuscal` for reference.