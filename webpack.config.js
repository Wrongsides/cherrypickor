const path = require('path');
const PATHS = {
    app: path.resolve(__dirname,'src/main/resources/public/'),
    build: path.resolve(__dirname,'build/resources/main/public/')
};

module.exports = {
    mode: 'none',
    devtool: 'source-map',
    entry: {
        app: PATHS.app + "/index.js"
    },
    output: {
        path: PATHS.build,
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader','css-loader']
            },
            {
                test: /\.js?/,
                exclude: /node_modules/,
                loader: 'babel-loader',
                options: {
                    presets:['env','react']
                }
            },
            {
                test: /\.png$/,
                use: "url-loader?limit=100000"
            },
            {
                test: /\.jpg$/,
                use: "file-loader"
            },
            {
                test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/,
                use: 'url-loader? limit=10000&mimetype=application/font-woff'
            },
            {
                test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
                use: 'url-loader?limit=10000&mimetype=application/octet-stream'
            },
            {
                test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
                use: 'file-loader'
            },
            {
                test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                use: 'url-loader?limit=10000&mimetype=image/svg+xml'
            }
        ]
    }
};