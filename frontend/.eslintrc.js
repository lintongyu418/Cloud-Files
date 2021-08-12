// https://eslint.org/docs/user-guide/configuring

module.exports = {
    root: true,
    parserOptions: {
        parser: 'babel-eslint',
    },
    env: {
        browser: true,
        "cypress/globals": true,
    },
    extends: ['@vue/standard', 'plugin:vue/recommended', 'prettier', 'plugin:json/recommended'],
    plugins: ['prettier', 'json', 'cypress'],
    rules: {
        'no-async-promise-executor' : 'off',
        'no-misleading-character-class' : 'off',
        'no-useless-catch' : 'off',
        'no-void': 'off',
        'max-len': ['error', 120],
        'no-console': 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
        'comma-dangle': [
            'error',
            {
                arrays: 'always-multiline',
                objects: 'always-multiline',
                imports: 'always-multiline',
                exports: 'always-multiline',
                functions: 'never',
            },
        ],
        'comma-spacing': [
            'error',
            {
                before: false,
                after: true,
            },
        ],
        "dot-notation": 0,
        'array-bracket-spacing': ['off', 'always'],
        'standard/object-curly-even-spacing': [0, 'either'],
        'standard/array-bracket-even-spacing': [0, 'either'],
        'standard/computed-property-even-spacing': [2, 'even'],
        'standard/no-callback-literal': [2, ['cb', 'callback']],
        'vue/no-v-html': 'off',
        'vue/max-attributes-per-line': [
            2,
            {
                singleline: 20,
                multiline: {
                    max: 1,
                    allowFirstLine: false,
                },
            },
        ],
        'vue/no-parsing-error': [
            2,
            {
                'x-invalid-end-tag': false,
            },
        ],
    },
    globals: {
        google: true,
        ROSLIB: true,
        gojs: true,
        __VUE_APP_ENV__: true,
        __ENV__: true,
        __NODE_ENV__: true,
        __DEBUG__: true,
    },
}
