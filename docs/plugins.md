# Plugins

## Plugin list

Plugin | Fonction
-- | --
`countAuthors` | lists the authors
`countCommits` | counts the commits by authors

## Plugin options

### Options structure

There are three types of options:

- toggle options
- value options
- charts

#### The value options

They represent a key value options (i.e. width: 100).

A list of existing toggle options that are not specific to any plugin is:

Options | Type | What it means
--- | --- | ---
`displayName` | string | The name you want your plugin to show up as in the frontend
`width` | int | The width of the graph

### The toggle options

They represent on and off values (i.e. nocolor).

A list of existins value options that are not specific to any plugins is:

Options | What it means
--- | ---
`nocolor` | [TODO] makes the chart use black and white values

### The charts

This represents the different type of charts that are available.

The list of available charts are :

- bar
- pie
- doughnut
