# MobMoney

Pays players money for killing mobs. The money itself is an [InfPoints](https://github.com/Wyne10/InfPoints-public)
currency, so MobMoney doesn't store any balances of its own — it just decides how much a kill
is worth and hands it over.

## Requirements

- Paper/Bukkit 1.16 or later, Java 16 or later
- [InfPoints](https://github.com/Wyne10/InfPoints-public) (required — that's where the currency lives)
- [CommandAPI](https://docs.commandapi.dev/) (optional, for `/mobmoney reload`)
- [Storm](https://github.com/Wyne10/Storm-public) (optional, for a temporary payout booster)

## Setting it up

Drop the jar in `plugins/`, start the server once, then open `plugins/MobMoney/config.yml`.

Payouts are set per mob under `drop`, as a range the amount is rolled from:

```yaml
drop:
  ANIMALS: 1.0..3.0
  MONSTERS: 5.0..10.0
  PIG: 20.0..55.0
```

Keys are either a category (`ANY`, `ANIMALS`, `MONSTERS`, `AMBIENT`, `WATER_ANIMALS`) or a
single [entity type](https://jd.papermc.io/paper/1.16.5/org/bukkit/entity/EntityType.html).
A specific mob wins over its category, so in the example above pigs pay 20–55 even though
they're animals. Anything you don't list pays nothing.

Multipliers are permission-based. The killer gets the highest multiplier they have permission
for, and 1.0 if they have none:

```yaml
multiplier:
  default:
    multiplier: 1.0
    permission: 'group.default'
  vip:
    multiplier: 1.5
    permission: 'group.vip'
```

`xp-multiplier` works exactly the same way, except it scales the experience the mob drops
instead of the money. The entry names (`default`, `vip`) are just labels, call them whatever
you like.

The rest of the file is generated for you:

```yaml
currency:
  # Key of the InfPoints point kills are paid in
  currencyKey: 'primary'

message:
  showChatMessage: false
  showActionBarMessage: true
```

`currencyKey` has to match a point key from InfPoints' own config. The two message options
decide whether the player is told about the payout in chat, on the action bar, both or
neither.

## Messages

Everything the plugin says lives in `plugins/MobMoney/lang/en.yml` (a Russian `ru.yml` ships
too; pick one with `lang` at the top of the config). Mob names are looked up by entity type,
so adding

```yaml
ZOMBIE: "a zombie"
```

makes the message say "You killed a zombie" instead of falling back to Minecraft's own name.

## Command

`/mobmoney reload` re-reads the config and the language files, no restart needed. It needs the
`mobmoney.reload` permission and CommandAPI installed.

## Temporary boosters

If you run Storm, MobMoney registers a `rich` effect type with it. Configure an instance of it
in Storm's config and any player under that effect earns more per kill:

```yaml
effect:
  rich:
    name: "Money booster"
    effect: rich
    multiplier: 2.0
```

Storm owns the duration and persistence side of it, so hand it out however you normally hand
out Storm effects.

## For developers

The API is on Maven Central:

```kotlin
compileOnly("io.github.wyne10:mobmoney-api:1.1.0")
```

It's one class. `MoneyDropEvent` fires before every payout, and you can read or change the
base drop and the multiplier, or cancel it to pay nothing:

```java
@EventHandler
public void onMoneyDrop(MoneyDropEvent e) {
    if (e.getPlayer().getWorld().getName().equals("arena"))
        e.setCancelled(true);
}
```

The final amount is `baseDrop * multiplier`, computed after the event, so changing either side
is enough.

## License

MIT.
