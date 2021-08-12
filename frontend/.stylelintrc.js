module.exports = {
  extends: ['stylelint-config-standard-scss', 'stylelint-config-prettier'],
  plugins: ['stylelint-order'],
  rules: {
    'no-empty-source': null,
    'no-descending-specificity': null,
    'no-duplicate-selectors': null,
    'declaration-block-no-duplicate-properties': null,
    'order/order': ['declarations', 'custom-properties', 'dollar-variables', 'rules', 'at-rules'],
  },
}
