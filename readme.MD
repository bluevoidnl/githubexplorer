See techDebt.MD for non supported features and work to be resolved

Considerations:

- not reading the isPrivate field from Github as per their documentation it is legacy:
/**
    * If private is true, the repository is restricted to specific users or teams with explicit access (e.g., the owner
    * and invited collaborators).
    * If private is false, the repository is accessible to a broader audience, but this doesn’t fully specify how
    * accessible it is—it could be public or, in some cases, internal (for enterprise accounts).
    *
    * This field is there for github historical reasons
    */