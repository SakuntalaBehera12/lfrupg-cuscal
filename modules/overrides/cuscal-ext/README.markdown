# Custom filter AuthRedirectFilter

## Purpose

Force overriding home page for logged in user.

## Configuration

- Configuration is under __Control Panel > System Settings > category.cuscal-filter__
- When `auth-filter-enabled` is checked, if users access `/home` or `/web/cuscal` or `/web/cuscal/` or `/web/cuscal/home`, the page content will show content from page declared in field `logged-in-home`.
- Default value of `logged-in-home` is `/web/cuscal/LoggedInhome`