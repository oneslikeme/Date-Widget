// Scriptable (iOS) widget: shows the dates 29, 22, 14 and 7 days before today.
// Setup: install Scriptable, paste this script, then add a Scriptable widget
// to the home screen and choose this script.

const OFFSETS = [29, 22, 14, 7];

const BG = Color.dynamic(new Color("#FFFFFF"), new Color("#1C1C1E"));
const FG = Color.dynamic(new Color("#000000"), new Color("#FFFFFF"));
const DIM = Color.dynamic(new Color("#6E6E73"), new Color("#98989D"));

const now = new Date();
const fmt = new DateFormatter();
fmt.dateFormat = "EEE, MMM d, yyyy";

function daysAgo(n) {
  // Calendar math (not 24h * n) so DST shifts don't change the day.
  const d = new Date(now.getFullYear(), now.getMonth(), now.getDate() - n);
  return d;
}

const w = new ListWidget();
w.backgroundColor = BG;
w.setPadding(12, 14, 12, 14);

OFFSETS.forEach((n, i) => {
  if (i > 0) w.addSpacer(6);
  const row = w.addStack();
  row.layoutVertically();
  const label = row.addText(`${n} days ago`);
  label.font = Font.mediumSystemFont(11);
  label.textColor = DIM;
  const date = row.addText(fmt.string(daysAgo(n)));
  date.font = Font.semiboldSystemFont(15);
  date.textColor = FG;
  date.minimumScaleFactor = 0.7;
  date.lineLimit = 1;
});

// Ask iOS to refresh just after the next midnight.
w.refreshAfterDate = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1, 0, 1);

if (config.runsInWidget) {
  Script.setWidget(w);
} else {
  await w.presentMedium();
}
Script.complete();
