# ADT resources tracking in codebase - apply to non-product environments
- Current situation, ADT code is not populated automatically from codebase to Liferay instances.
- This folder is used to keep track of ADT code changes.
- ADT code here will be the source of truth for all non-product environments. The developers have obligation to apply changes from these ADT code to all possible non-product environments. Cuscal keeps rights to make changes in their PROD environment and may need to sync manually in some ways.
- PLEASE NOTE: Changes in ADT code from a Cuscal non-product environment without committing to codebase are not guaranteed to be kept. Someone may bring ADT code from this folder and apply to a Cuscal non-product environment as the latest effective implementation.

# The process to modify and sync ADT code between environments
- When there is a need of ADT change, make the changes in local workspace, test it locally and commit to your folk.
- Send a PR to upstream.
- When the PR is approved and merged, the developer should apply to all Cuscal non-product environments.

# ADT population scope
- We will put ADT that can be shared and used by all sites under `global`. This mean, ADT under folder `global` will be populated to site Global.
- For other site scope ADTs, will place them under the corresponding site with folder name using the friendly URL of site. For example: `cuscal` or `eft-connect`

# How to use left navigation menu ADT

This ADT applied to Navigation Menu OOT feature.

1. Create New Master Page (Include left Navigation Menu in a Grid Layout 3 cols, 9 cols)
2. Add new OOT Navigation Menu and Place it on 3 cols in Grid 
3. Configure it and choose Left Navigation Menu template if no template create once and paste that code from file left-navigation-ment-adt.ftl
