
### Outline

+ Sleep - Schedule 8.5 hour sleep shift starting at 22:00
+ Pre-Sleep - Schedule 2 hours of pre sleep directly before every sleep 
+ Post-Sleep - Schedule 1.5 hours of post-sleep directly after every sleep
+ Meals - Schedule 1 meal right after post-sleep, 1 meal right before pre-sleep, and another between 11 and 1 
+ Exercise - Schedule 1 hr prior to noon, 1.5 hour after noon
+ Research Hours - Populate remaining time with research

### Sleep

```ts
function ScheduleSleep() {
  return Goal.CoexistenceGoal({
    forEach: Discrete.Resource('/hour').equal('22'),
    activityTemplate: ActivityTemplates.Sleep({crewMember: 'ISSCDR', activityDurHr: 8.5}),
    startsAt: TimingConstraint.singleton(WindowProperty.START)
  });
}

export default ScheduleSleep;
```

### Pre-Sleep

```ts
function SchedulePreSleep() {
  return Goal.CoexistenceGoal({
    forEach: ActivityExpression.ofType(ActivityTypes.Sleep),
    activityTemplate: ActivityTemplates.PreSleep({crewMember: 'ISSCDR', activityDurHr: 2.0}),
    endsAt: TimingConstraint.singleton(WindowProperty.START)),
  });
}

export default SchedulePreSleep;
```

### Post-Sleep

```ts
function SchedulePostSleep() {
  return Goal.CoexistenceGoal({
    forEach: ActivityExpression.ofType(ActivityTypes.Sleep),
    activityTemplate: ActivityTemplates.PostSleep({crewMember: 'ISSCDR', activityDurHr: 1.5}),
    startsAt: TimingConstraint.singleton(WindowProperty.END)),
  });
}

export default SchedulePostSleep;
```
