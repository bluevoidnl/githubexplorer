
# Tech debt / not supported:

- pagination is not supported, it should support this in the future when the abnamrocoesd exceeds
the page limit of 100. At this moment abnamrocoesd has 35 repositories. 
- use StringResources for strings in the UI to support multiple languages 
- handle malformed urls in ExplorerViewModel.openInBrowser()
- no remember Scroll state of the LazyColumn when coming back to overview
- missing icon for the app
- when a repository was deleted but visible in the overview, the details of that repository may not
  be shown after data is updated

Skipped to gain time for part 2:
- missing unittest for openInBrowser()
- missing unittest for detecting network connection
- missing unittest for data to domain mapping in Repository

