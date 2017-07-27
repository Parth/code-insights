var webpack = require('webpack');
var path = require('path');

var APP_DIR = path.resolve(__dirname, 'src/');

var config = {
	entry: "./src/index.jsx",
	

	output: {
		filename: "bundle.js",
		path: __dirname
	},

	module : {
		loaders : [
			{
				test : /\.jsx?/,
				include : APP_DIR,
				loader : 'babel-loader',
				query: {
					presets: ['es2015', 'stage-0', 'react']
				}
			}
		]
	},

	resolve: {
		extensions: [".jsx", ".js"]
	},

	devServer: {
		port: 3000,
		disableHostCheck: true,
		historyApiFallback: {
			index: 'index.html'
		}
	},
};

module.exports = config;
