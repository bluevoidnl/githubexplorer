Considerations:

- This is a challenging assignment to do next to taking care of my children in the weekend 
and working the weekdays. Shortcuts were made to get to the end, everything that was skipped is 
mentioned in techdebt.MD

- I started with creating the viewmodel using TDD. First create the test, then add the implementation.
The lessons from the coding dojo that I followed learned me that in this way a more elegant 
implementation arises. And it leads to 100% code coverage. Except for the last part when I skipped 
adding unittest as time pressures.  

- I chose ktor as the network client, this because I do not have that much experience with it 
and it will be a requirement for every project that wants to use KMP,
As I understood KMP is not something that will happen soon at ABN AMRO, but still interesting to use 
ktor. Ktor is Not as elegant as retrofit though.

- I had not worked with Room before, all projects that I have worked on use plain SQLite, 
so this was a chance to use it and get a feel for it. Good first impression.

Dilemmas:
- with koin and mockk there is technically no reason to use interfaces especially in a small project,
YAGNI says: no need for it now. From the SOLID principals: Dependency Inversion so you can
decouple the code, so I added interfaces.

- How to represent a url in the domain? 
From a hexagonal architecture view: the data should know how to map itself to the domain. 
When using the android Uri and the app gets modularized then the data module would be forced to be an 
android module (compile time penalty) instead of a kotlin library. So Uri is not a good option in this case. 
Object Calisthenics says wrap primitives: so I added a value class for Url. A url is not just a String. 

- no attention was given to theming, app icon, fonts or string resources, little attention to layout

- not reading the isPrivate field from Github as per their documentation it is legacy:
 "If private is true, the repository is restricted to specific users or teams with explicit access (e.g., the owner
 and invited collaborators).
 If private is false, the repository is accessible to a broader audience, but this doesn’t fully specify how
 accessible it is—it could be public or, in some cases, internal (for enterprise accounts).

 This field is there for github historical reasons"
