
# Tech debt / not supported:
- when a repository was deleted but visible in the overview, the details of that repository can not 
be shown after data is updated
- pagination is not supported, it should support this in the future when the abnamrocoesd exceeds
the page limit of 100. At this moment abnamrocoesd has 35 repositories. 
- use StringResources for strings in the UI to support multiple languages 
- handle malformed urls in ExplorerViewModel.openInBrowser()
- no unittest for openInBrowser()
- 