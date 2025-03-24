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

- One can debate whether the loading logic of the data should be in the DataRepository or in the usecase, 
I choose the usecase as in part 2 network availability will have an impact there.

- no attention was given to theming, fonts or string resources, little attention to layout

- not reading the isPrivate field from Github as per their documentation it is legacy:
/**
    * If private is true, the repository is restricted to specific users or teams with explicit access (e.g., the owner
    * and invited collaborators).
    * If private is false, the repository is accessible to a broader audience, but this doesn’t fully specify how
    * accessible it is—it could be public or, in some cases, internal (for enterprise accounts).
    *
    * This field is there for github historical reasons
    */