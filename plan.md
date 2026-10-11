# Build Tracker plugin plan

## settings.yaml
```yaml
edit_review_after_approved: false
marker_type_track: # from bottom to top
  incomplete: 
    name: "Incomplete"
    icon: "RED_WOOL"
  pending: 
    name: "Pending"
    icon: "YELLOW_WOOL"

track_resolve_mode: manual # /auto
show_on_dynmap: false

```

## new commands
- `/marker <marker_id> info` bt.user
- `/marker <marker_id> edit ...` bt.user + creator/bt.admin perm check
  - `... name <name>`
  - `... type <marker_type>`
  - `... description [description]`
  - `... location <location>`
  - `... dynmap_visible <visible?>`
- `/marker <marker_id> advance` bt.user + creator/bt.admin
- `/marker <marker_id> delete` bt.user + creator/bt.admin perm check

- `/task <marker_id> create <player> [instructions]` bt.user + creator/bt.admin perm check
- `/task <marker_id> info` bt.user
- `/task <marker_id> mark (wip|done) [comment]` bt.user + assigned/bt.reviewer perm check NO CONSOLE
- `/task <marker_id> remove` bt.user + creator/bt.admin perm check

- `/bt list markers` bt.user
- `/bt list tasks` bt.user
- `/bt mark <marker_id> <marker_type> [<marker_name>]` bt.user NO CONSOLE
- `/bt info` bt.user
- `/bt reload` bt.admin
