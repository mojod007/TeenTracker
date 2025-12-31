# TODO: Fix User Assignment Fields Loading Issue

## Completed Tasks
- [x] Analyzed the issue: Establishment and depot fields not loading in user form
- [x] Investigated UserController and found URL mismatch for etablissement-service API calls
- [x] Fixed ETAB_SERVICE_URL from "http://localhost:8080/etablissements/api" to "http://localhost:8080/etablissements/api/etablissements"
- [x] Verified the EtablissementRestController endpoints match the corrected URLs

## Next Steps
- [x] Restart the services to apply the changes (services started via runprojet.bat)
- [ ] Test the user form to ensure establishments and depots load properly
- [ ] Verify that user assignments can be saved successfully

# TODO: Add View Button for User Details

## Tasks
- [x] Add @GetMapping("/view/{id}") method in UserController to display user details
- [x] Create user-details.html template to show user information
- [x] Add "View" button in actions column of user-list.html
