# TODO: Fix Empty Attributes in User-Form Page

## Completed Tasks
- [x] Identified the issue: UserController was using hardcoded localhost:8080 URL instead of service name for load balancing.
- [x] Updated ETAB_SERVICE_URL in UserController.java from "http://localhost:8080/api/etablissements" to "http://gateway-service/api/etablissements".
- [x] Added logging to fetchEtablissements() method to verify data retrieval.

## Next Steps
- [ ] Restart the user-service to apply the changes.
- [ ] Test the user-form page to verify that allEtablissements, allDepots, assignedEtabIds, and assignedDepotIds are populated.
- [ ] Check the user-service logs for the "Fetched etablissements:" message to confirm data is being retrieved.
- [ ] Ensure Eureka server and all services (gateway, etablissement-service, user-service) are running for load balancing to work.
